package discoverer;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

public class FileReaderTest {

    @Test
    public void test() {
        List<String> s = FileToStringList.convert("/home/asch/tmp/ondra.txt");
        System.out.println(s);
    }

}
