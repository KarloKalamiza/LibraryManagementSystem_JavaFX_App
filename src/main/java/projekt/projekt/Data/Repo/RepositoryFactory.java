package projekt.projekt.Data.Repo;

import projekt.projekt.Data.SQL.SqlRepository;

public class RepositoryFactory {

    public RepositoryFactory() {
    }

    public static Repository getRepository() throws Exception{
        return new SqlRepository();
    }
}
