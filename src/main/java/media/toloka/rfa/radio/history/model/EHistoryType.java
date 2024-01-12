package media.toloka.rfa.radio.history.model;

public enum EHistoryType {
    History_UserChangeConfirmInfo("UserChangeConfirmInfo"),
    History_UserInfoSave("UserInfoSave"),
    History_UserChangeInfo("UserChangeInfo"),
    History_UserCreateContract("UserCreateContract"),
    History_UserPayContract("UserPayContract"),
    History_UserPauseContract("UserPauseContract"),
    History_UserResumeContract("UserResumeContract"),
    History_UserStopContract("UserStopContract"),
    History_UserCreate("UserCreate"),
    History_UserSendMail("UserSendMail"),
    History_UserSendMailSetPassword("UserSendMailSetPassword"),
    History_UserSetPassword("UserSetPassword"),
    History_UserDelete("UserDelete"),
    History_UserSuspend("UserSuspend"),
    Hisrory_UserLock("UserLock"),
    Hisrory_UserUnLock("UserUnLock"),
    History_StatiionCreate("StationCreate"),
    History_StatiionChangeStatus("StationChangeStatus"),
    History_StatiionChangeName("StationChangeName"),
    History_DocumentCreate("DocumentCreate"),
    History_DocumentChange("DocumentChange"),
    History_DocumentConfirm("DocumentConfirm"),
    History_DocumentReject("DocumentReject"),
    History_ContractCreate("ContractCreate"),
    History_ContractChange("ContractChange"),
    History_ContractConfirm("ContractConfirm"),
    History_ContractStart("ContractStart"),
    History_ContractEnd("ContractEnd"),
    History_ContractPause("ContractPause"),
    History_Contractresume("ContractResume"),
    History_ContractReject("ContractReject");


    public final String label;

    private EHistoryType(String label) {
        this.label = label;
    }
}
