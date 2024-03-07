package media.toloka.rfa.media.store.Interface;

import java.io.InputStream;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public interface StoreInterface {

    public InputStream GetFileFromStore (String uuid);
    public String PutFileToStore (InputStream inputStream, String filepatch);
    public Boolean DeleteFileInStore (String uuid);


//    public default InputStream GetFileFromStore (String uuid) {
//        InputStream is = null;
//        return is;
//    }
//    public default String PutFileToStore (InputStream inputStream) {
//
//        // check file exist
//        // create new version
//        // put file to store
//        String uuid= UUID.randomUUID().toString();
//
//        return uuid;
//    }
//    public default Boolean DeleteFileInStore (String uuid) {
//        return true;
//    }

}
