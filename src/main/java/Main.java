import lombok.extern.slf4j.Slf4j;
import org.example.hibernateUtil.HibernateUtil;
import org.example.userInterface.CLIInterface;

@Slf4j
public class Main {
    public static void main(String[] args) {
            HibernateUtil.getSessionFactory();
            CLIInterface.mainMenu();
            log.info("Executed!");


    }
}