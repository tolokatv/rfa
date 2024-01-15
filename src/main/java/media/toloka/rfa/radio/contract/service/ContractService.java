package media.toloka.rfa.radio.contract.service;

import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.contract.model.Contract;
import media.toloka.rfa.radio.contract.repo.ContractRepo;
import media.toloka.rfa.security.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ClientService clientService;




    @Autowired
    private ContractRepo contractRepo;
//    public List<Contract> listContractByUser(Users user) {
//        return contractRepo.findContractByUser(user);
//    }

    public void saveContract(Contract contract) {
        contractRepo.save(contract);
    }

    public List<Contract> listContractByUser(Users user) {
        Clientdetail cl = clientService.getClientDetail(clientService.GetCurrentUser());
        return contractRepo.findByClientdetail(cl);
    }
}
