package media.toloka.rfa.radio.store.Reposirore;

import media.toloka.rfa.radio.model.Clientdetail;
import media.toloka.rfa.radio.model.Station;
import media.toloka.rfa.radio.store.model.EStoreFileType;
import media.toloka.rfa.radio.store.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

//public interface StoreRepositore extends JpaRepository<Store, Long> {
public interface StoreRepositore extends PagingAndSortingRepository<Store, Long>,
        JpaRepository<Store, Long>
{
    List<Store> findAllByClientdetail(Clientdetail clientdetail);
    Store getByFilenameAndClientdetail(String filename, Clientdetail cd);
//    Pageable PageRequest.of(int pagenumber, int pagecount);
//    Store getById(Long id);
//    void save(Store store);

//    List<Store> findByClientdetailByStorefiletypeOrederByCreatedateByAsc(Clientdetail clientdetail, EStoreFileType storefiletype);
    Page<Store> findByStorefiletype(Pageable pageable, EStoreFileType storefiletype);

    Page findByClientdetail(Pageable storePage, Clientdetail cd);
}
