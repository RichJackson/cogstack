package uk.ac.kcl.testexecutionlisteners;


import org.junit.Ignore;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import uk.ac.kcl.utils.DbmsTestUtils;
import uk.ac.kcl.utils.TestUtils;

/**
 * Created by rich on 03/06/16.
 */
@Ignore
public class DeidTestExecutionListener extends AbstractTestExecutionListener {

    public DeidTestExecutionListener(){}

    @Override
    public void beforeTestClass(TestContext testContext) {
        DbmsTestUtils dbTestUtils =
                testContext.getApplicationContext().getBean(DbmsTestUtils.class);
        dbTestUtils.createJobRepository();
        dbTestUtils.createBasicInputTable();
        dbTestUtils.createBasicOutputTable();
        dbTestUtils.createDeIdInputTable();
        TestUtils testUtils =
                testContext.getApplicationContext().getBean(TestUtils.class);
        Environment env = testContext.getApplicationContext().getBean(Environment.class);
        testUtils.deleteESTestIndexAndSetUpMapping();
        testUtils.insertDataIntoBasicTable(env.getProperty("tblInputDocs"),true,1,75,false);
        //don't use any mutations for integration tests (see acceptance tests for usage)
        testUtils.insertTestDataForDeidentification(env.getProperty("tblIdentifiers"),env.getProperty("tblInputDocs"),
                0,false);
    }

}
