package media.toloka.rfa.repository;


import media.toloka.rfa.model.Clientaddress;
import media.toloka.rfa.model.Clientdetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientAddressRepository extends JpaRepository<Clientaddress, Long> {

    Clientaddress getById(Long id);

    List<Clientaddress> findByClientdetail(Clientdetail clientdetail);

    //Id(Long id);

}
