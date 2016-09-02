package com.persistentbit.jjson.security;

import com.persistentbit.jjson.mapping.JJMapper;
import com.persistentbit.jjson.mapping.impl.JJsonException;
import com.persistentbit.jjson.nodes.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

/**
 * JJSigning makes it possible to sign a JJNode.<br>
 * This can be used to generate/verify JJNode or to generate String tokens from a JJNode.<br>
 * Example Usage:<br>
 * <pre>{@code
 * JJSigning signing = new JJSigning("YourSecretKey","SHA-256");
 * JJNode signed = signing.sign(unsigned); //signed result as a JJNode
 * String token = signing.signAsString(unsigned); //signed result as a string value
 *
 * Optional<JJNode> verified = signing.unsigned(signed);   //empty when verification failed
 * Optional<JJNode> verifiedToken = signing.unsignedFromString(token); //empty when verification failed
 *
 * }</pre>
 */
public class JJSigning {
    private final char[]   secret;
    private final String   hashAlgorithm;


    /**
     * Construct with default "SHA-256" algorithm
     * @param secret    The secret to use
     */
    public JJSigning(String secret){
        this(secret,"SHA-256");
    }

    /**
     *
     * @param secret The secret to use
     * @param hashAlgorithm the hash-algorithm to use. (see {@link MessageDigest})
     */
    public JJSigning(String secret, String hashAlgorithm){
        this.secret = Objects.requireNonNull(secret.toCharArray());
        this.hashAlgorithm = hashAlgorithm;
    }


    /**
     * Sign a JJNode by creating a hash value and
     * wrapping the data and secret in a new json-object with properties "data" and "signed";
     * @param unsigned The JJNode to sign
     * @return A new JJNodeObject with the data and the hash code
     */
    public JJNode  sign(JJNode unsigned){
        String str = JJPrinter.print(false,unsigned)+"."+new String(secret);
        String signed = hashAlgorithm+"."+sign(str,hashAlgorithm);
        return new JJNodeObject().plus("data",unsigned).plus("signed",new JJNodeString(signed));
    }

    /**
     * Wrapper  around {@link #sign(JJNode)} that returns just a base64 encoded string of the signed
     * JJNode.<br>
     * Can be used to create a Authentication token.<br>
     * @param unsigned The unsinged JJNode
     * @return Base64 encode string.
     */
    public String signAsString(JJNode unsigned){
        try {
            return printBase64Binary(JJPrinter.print(false, sign(unsigned)).getBytes("UTF-8"));
        }catch (Exception e){
            throw new JJsonException(e);
        }
    }

    /**
     * Verify and return the Unsinged JJNode object from a signed JJNode object
     * @param signed The signed JJNode
     * @return empty if verification failed, or unsigned version of the JJNode
     * @see #sign(JJNode)
     */
    public Optional<JJNode>    unsigned(JJNode signed){
        JJNodeObject obj = signed.asObject().get();
        String signedSecret= obj.getValue().get("signed").asString().get().getValue();
        int i = signedSecret.indexOf('.');
        String algorithm = signedSecret.substring(0,i);
        signedSecret = signedSecret.substring(i+1);
        String str = JJPrinter.print(false,obj.getValue().get("data"))+"."+new String(secret);
        String sstr = sign(str,algorithm);
        if(sstr.equals(signedSecret)){
            return Optional.of(obj.getValue().get("data"));
        }
        return Optional.empty();
    }
    /**
     * Verify and return the Unsinged JJNode object from a signed String
     * @param str The signed JJNode as Base64 encode string
     * @return empty if verification failed, or unsigned version of the JJNode
     * @see #signAsString(JJNode)
     */
    public Optional<JJNode>    unsignedFromString(String str){
        try {
            JJNode node = JJParser.parse(new String(parseBase64Binary(str),"UTF-8"));
            return unsigned(node);
        } catch (UnsupportedEncodingException e) {
            throw new JJsonException(e);
        }
    }

    static public String sign(String data,String algorithm){
        try{
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data.getBytes("UTF-8"));
            byte[] mdbytes = md.digest();
            return printBase64Binary(mdbytes);
        }catch(Exception e){
            throw new JJsonException(e);
        }
    }


    static public class TestClass{
        private final int id;
        private final String userName;

        public TestClass(int id, String userName) {
            this.id = id;
            this.userName = userName;
        }
    }

    static public void main(String...args){
        JJMapper mapper = new JJMapper();
        JJNodeObject unsigned = mapper.write(new TestClass(1234,"userx")).asObject().get();
        System.out.println("Unsigned: " + JJPrinter.print(true,unsigned));
        JJSigning signing = new JJSigning("Dit is een test signing key","SHA-256");
        JJNode signed = signing.sign(unsigned);
        String token = signing.signAsString(unsigned);



        System.out.println("Signed: " + JJPrinter.print(true,signed));

        System.out.println("SignedAsString: " + token);

        System.out.println("Verified: " + signing.unsigned(signed));
        System.out.println("Verified from token: " + signing.unsignedFromString(token));


        JJNode changed = signed.asObject().get().plus("data", mapper.write(new TestClass(1234,"usery")));

        System.out.println("changed: " + JJPrinter.print(true,changed));
        System.out.println("VerifiedChanged: " + signing.unsigned(changed));
        long start = System.currentTimeMillis();
        for(int t=0; t<100000;t++){
            signing.unsignedFromString(token).get();
        }
        long time = System.currentTimeMillis()-start;

        System.out.println("Time:" + (time));
    }

}
