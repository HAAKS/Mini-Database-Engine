import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.Iterator;

public class Page implements Serializable {

	ArrayList<Hashtable<String, String>> table;
	int pageIterator;
	int column;
	int count;

	public Page() {

		Properties prop = new Properties();
		InputStream reader = null;
		try {

			reader = new FileInputStream("DBApp.properties");

			prop.load(reader);
			String temp = (prop.getProperty("MaximumRowsCountinPage"));
			pageIterator = Integer.parseInt(temp);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		table = new ArrayList<Hashtable<String, String>>();
		count = 0;
	}

	public void insert(Hashtable<String, String> htblColNameValue) {
		if (count <= pageIterator) {
			table.add(htblColNameValue);
			count++;
		}
	}

	public ArrayList<Hashtable<String, String>> search(
			Hashtable<String, String> htblColNameValue) {
		String str; // keys of htblColNameValue
		String str2; // keys of table
		String compare; // values of htblColNameValue
		String compare2; // values of table
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String> hashResult = new Hashtable<String, String>();
		Set<String> keys = htblColNameValue.keySet();

		Iterator<String> itr = keys.iterator();

		for (int i = 0; i < htblColNameValue.size(); i++) {
			while (itr.hasNext()) {
				str = itr.next();
				compare = htblColNameValue.get(str);

				for (int j = 0; j < table.size(); j++) {
					Set<String> tableKeys = table.get(j).keySet();
					Iterator<String> itr2 = tableKeys.iterator();
					while (itr2.hasNext()) {
						str2 = itr2.next();
						compare2 = htblColNameValue.get(str2);

						if (str == str2) {
							if (compare == compare2) {
								hashResult.put(str, compare);
								result.add(hashResult);
							}
						}
					}
				}
			}
		}
		return result;

	}

	public boolean isFull() {
		if (count == pageIterator)
			return true;
		else
			return false;

	}

	public void delete(Hashtable<String, String> htblColNameValue) {
		ArrayList<Hashtable<String, String>> deleteList = search(htblColNameValue);
		table.remove(deleteList);

	}

	public Hashtable<String, String> searchbyname(String columnName,
			int pageNumber) {
		Hashtable<String, String> compare;
		Hashtable<String, String> result = new Hashtable<String, String>();
		String search;
		for (int i = 0; i < table.size(); i++) {
			compare = table.get(i);
			Set<String> keys = compare.keySet();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext()) {
				search = itr.next();
				if (search == columnName) {
					String convert = pageNumber + "" + i + "";
					result.put(compare.get(search), convert);
				}
			}
		}
		return result;

	}
}