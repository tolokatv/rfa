package media.toloka.rfa.radio.store.Interface;

import media.toloka.rfa.radio.store.model.EStoreFileType;
import media.toloka.rfa.radio.model.Clientdetail;

import java.io.InputStream;

public interface StoreInterface {

    public InputStream GetFileFromStore (String uuid);
    public String PutFileToStore (InputStream inputStream, String filepatch, Clientdetail cd, EStoreFileType storeFileType);
//    public String PutFileToStore(InputStream inputStream, String filename, Clientdetail cd, EStoreFileType storeFileType)
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
