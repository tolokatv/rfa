package media.toloka.rfa.radio.client.repository;


import media.toloka.rfa.radio.client.model.Clientaddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAddressRepository extends JpaRepository<Clientaddress, Long> {

    Clientaddress getById(Long id);

    //Id(Long id);

}
