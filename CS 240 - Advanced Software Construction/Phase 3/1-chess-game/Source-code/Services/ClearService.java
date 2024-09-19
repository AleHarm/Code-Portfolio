package Services;

import DatabaseTEMP.DatabaseTEMP;
import Services.Responses.ClearResponse;

public class ClearService {

    public static ClearResponse clear(){

        ClearResponse clearResponse = new ClearResponse();

        //Clear database
        DatabaseTEMP.getDatabase().clear();

        //Return info
        clearResponse.setSuccess(true);
        return clearResponse;
    }
}
