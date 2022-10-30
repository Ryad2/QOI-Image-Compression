package cs107;

/**
 * "Quite Ok Image" Encoder
 * @apiNote Second task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.2
 * @since 1.0
 */
public final class QOIEncoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private QOIEncoder(){}

    // ==================================================================================
    // ============================ QUITE OK IMAGE HEADER ===============================
    // ==================================================================================

    /**
     * Generate a "Quite Ok Image" header using the following parameters
     * @param image (Helper.Image) - Image to use
     * @throws AssertionError if the colorspace or the number of channels is corrupted or if the image is null.
     *  (See the "Quite Ok Image" Specification or the handouts of the project for more information)
     * @return (byte[]) - Corresponding "Quite Ok Image" Header
     */
    public static byte[] qoiHeader(Helper.Image image){

        assert image!=null;
        assert image.channels()==QOISpecification.RGB || image.channels()==QOISpecification.RGBA;
        assert image.color_space()==QOISpecification.sRGB || image.color_space()==QOISpecification.ALL;


        byte[] width = ArrayUtils.fromInt(image.data()[0].length);
        byte[] height = ArrayUtils.fromInt(image.data().length);

        byte[] result = new byte[QOISpecification.HEADER_SIZE];

        for (int i = 0; i < 4; i++)  result[i] = QOISpecification.QOI_MAGIC[i];
        for (int i = 4; i < 8; i++)  result[i] = width[i-4];
        for (int i = 8; i < 12; i++)  result[i] = height[i-8];
        result[12] = image.channels();
        result[13] = image.color_space();

        return result;
    }

    // ==================================================================================
    // ============================ ATOMIC ENCODING METHODS =============================
    // ==================================================================================

    /**
     * Encode the given pixel using the QOI_OP_RGB schema
     * @param pixel (byte[]) - The Pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) - Encoding of the pixel using the QOI_OP_RGB schema
     */
    public static byte[] qoiOpRGB(byte[] pixel){

        assert pixel.length==4;

        byte[] result = new byte[4];

        result[0] = QOISpecification.QOI_OP_RGB_TAG;
        for (int i = 0; i < 3; i++)  result[i+1] = pixel[i];

        return result;
    }

    /**
     * Encode the given pixel using the QOI_OP_RGBA schema
     * @param pixel (byte[]) - The pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) Encoding of the pixel using the QOI_OP_RGBA schema
     */
    public static byte[] qoiOpRGBA(byte[] pixel){

        assert pixel.length==4;

        byte[] result = new byte[5];

        result[0] = QOISpecification.QOI_OP_RGBA_TAG;
        for (int i = 0; i < 4; i++) result[i+1] = pixel[i];

        return result;
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index){
        assert index >= 0 && index <= 63;
        return ArrayUtils.wrap(index);
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_DIFF schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpDiff(byte[] diff){
        assert diff!=null;
        assert diff.length==3;
        for ( byte di:diff) assert di<=1 && di>=-2;


        byte result=QOISpecification.QOI_OP_DIFF_TAG;
        for(int i=diff.length-1; i>=0; i--){
            byte x= (byte)((diff[2-i]+2)<<2*i);
            result= (byte) (result | x) ;
        }


        return ArrayUtils.wrap(result);
    }//MAKE IT BETTER

    /**
     * Encode the difference between 2 pixels using the QOI_OP_LUMA schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints
     * or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpLuma(byte[] diff){
        assert diff!=null;
        assert diff.length==3;
        assert diff[1]>-33 && diff[1]<32 ;
        for(int i=0;i<diff.length;i+=2)assert diff[i]-diff[1]>-9 && diff[i]-diff[1]<8 ;

        byte[] result=new byte[2];
        result[0]=(byte)(QOISpecification.QOI_OP_LUMA_TAG + (32+diff[1]));
        result[1]=(byte)(((diff[0]-diff[1]+8)<<4) + diff[2]-diff[1]+8);

        return result;
    }

    /**
     * Encode the number of similar pixels using the QOI_OP_RUN schema
     * @param count (byte) - Number of similar pixels
     * @throws AssertionError if count is not between 0 (exclusive) and 63 (exclusive)
     * @return (byte[]) - Encoding of count
     */
    public static byte[] qoiOpRun(byte count){
        assert count>=1 && count<=62;
        byte result =(byte)(QOISpecification.QOI_OP_RUN_TAG+(count-1));

        return ArrayUtils.wrap(result);
    }

    // ==================================================================================
    // ============================== GLOBAL ENCODING METHODS  ==========================
    // ==================================================================================

    /**
     * Encode the given image using the "Quite Ok Image" Protocol
     * (See handout for more information about the "Quite Ok Image" protocol)
     * @param image (byte[][]) - Formatted image to encode
     * @return (byte[]) - "Quite Ok Image" representation of the image
     */
    public static byte[] encodeData(byte[][] image){
        assert image!=null;
        for(byte[] im: image) assert  im !=null && im.length==4;

        byte[] prvPix= QOISpecification.START_PIXEL;
        byte[][] hashTab=new byte[64][4];
        int counter=0;

        for(int i=0;i< image.length;i++){
            if (ArrayUtils.equals(image[i],prvPix)) {

                counter ++
            }


        }

    }

    /**
     * Creates the representation in memory of the "Quite Ok Image" file.
     * @apiNote THE FILE IS NOT CREATED YET, THIS IS JUST ITS REPRESENTATION.
     * TO CREATE THE FILE, YOU'LL NEED TO CALL Helper::write
     * @param image (Helper.Image) - Image to encode
     * @return (byte[]) - Binary representation of the "Quite Ok File" of the image
     * @throws AssertionError if the image is null
     */
    public static byte[] qoiFile(Helper.Image image){
        return Helper.fail("Not Implemented");
    }

}