package media.toloka.rfa.radio.store.Service;


import media.toloka.rfa.radio.dropfile.service.FilesService;
import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.store.Reposirore.StoreRepositorePagination;
import media.toloka.rfa.radio.store.model.EStoreFileType;
import media.toloka.rfa.radio.store.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class StoreService {

    final Logger logger = LoggerFactory.getLogger(StoreService.class);

    @Autowired
    private StoreRepositorePagination storeRepositore;

    @Autowired
    private FilesService filesService;

    public List<Store> GetAllByClientId(Clientdetail cd) {
        return storeRepositore.findAllByClientdetail(cd);
    }

    public Page GetStorePage(int pageNumber, int pageCount) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = storeRepositore.findAll(storePage);
        return page;
    }

    public Page GetStorePageByClientDetail(int pageNumber, int pageCount, Clientdetail cd) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = storeRepositore.findByClientdetail(storePage,cd);
        return page;

    }

    public Store SaveStoreItemInfo(Store storeitem) {
        storeRepositore.save(storeitem);
        return storeitem;
    }

    public Store SaveStoreItemInfo(Store storeitem, Path destination, EStoreFileType eStoreFileType, Clientdetail cd) {
        if (storeitem == null) {
            storeitem = new Store();
            storeitem.setFilepatch(destination.toAbsolutePath().toString());
            storeitem.setStorefiletype(eStoreFileType);
            storeitem.setClientdetail(cd);
            storeitem.setFilename(destination.getFileName().toString());
            storeitem.setContentMimeType(filesService.GetMediatype(destination));
            storeitem.setFilelength(filesService.GetMediaLength(destination));

        }
        storeRepositore.save(storeitem);
        return storeitem;
    }

    public Store GetByFilenameByClientDetail(String fileName, Clientdetail cd) {
        return storeRepositore.getByFilenameAndClientdetail(fileName,cd);
    }

    public Page GetStorePageItemType(int pageNumber, int pageCount, EStoreFileType eStoreFileType) {
        Pageable storePage = PageRequest.of(pageNumber, pageCount);
        Page page = storeRepositore.findByStorefiletype(storePage, eStoreFileType);
        return page;

    }
}
