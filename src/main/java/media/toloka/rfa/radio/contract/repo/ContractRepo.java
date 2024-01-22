package media.toloka.rfa.radio.contract.repo;


import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.contract.model.Contract;
import media.toloka.rfa.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepo extends JpaRepository<Contract, Long> {

//    Optional
    List<Contract> findByClientdetail(Clientdetail cd);
    Contract getById(Long id);
}
