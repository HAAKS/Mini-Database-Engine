import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import BPlusTree.BTree;

public class DBApp {
	public final static String curDir = System.getProperty("user.dir");
	public final static String dataDir = curDir + "/data/";
	public final static String metaData = "metadata.csv";
	Properties prop;
	public static ArrayList<Table> tables;
	public static ArrayList<BTree<String, String>> BPlusTrees = new ArrayList<BTree<String, String>>();

	public void init() {

		prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream("DBApp.properties");

			// set the properties value
			prop.setProperty("MaximumRowsCountinPage", "200");
			prop.setProperty("BPlusTreeN", "20");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException, IOException {
		Table temp = new Table(strTableName);
		tables.add(temp);
		temp.createTable(strTableName, htblColNameType, htblColNameRefs,
				strKeyColName);

	}

	public void createIndex(String strTableName, String strColName)
			throws DBAppException {
		String key;
		String value;
		ArrayList<Hashtable<String, String>> searchresult;
		Hashtable<String, String> compare;
		BTree<String, String> btree = new BTree<String, String>();
		for (int index = 0; index < tables.size(); index++) {
			if (tables.get(index).name == strTableName) {
				searchresult = tables.get(index).searchbyname(strTableName);
				for (int i = 0; i < searchresult.size(); i++) {
					compare = searchresult.get(i);
					Set<String> keys = compare.keySet();
					Iterator<String> itr = keys.iterator();
					while (itr.hasNext()) {
						key = itr.next();
						value = compare.get(key);
						btree.put(key, value);
					}

				}
				BPlusTrees.add(btree);
			}

		}

	}

	public void createMultiDimIndex(String strTableName,
			Hashtable<String, String> htblColNames) throws DBAppException {
	}
 
	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws DBAppException,
			FileNotFoundException, IOException {
		Table.insert(htblColNameValue, strTableName);
		
	}

	public void deleteFromTable(String strTableName,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {
		for (int index = 0; index < tables.size(); index++) {
			if (tables.get(index).name == strTableName) {
				tables.get(index).delete(htblColNameValue);
			}
		}

	}

	public Iterator selectFromTable(String strTable,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		for (int index = 0; index < tables.size(); index++) {
			if (tables.get(index).name == strTable) {
				result = tables.get(index).search(htblColNameValue);
			}
		}
		Iterator itr = result.iterator();
		return itr;
	}

	public void saveAll() throws DBEngineException {
		// CREATE FOLDER TABLES,

	}

	public static void main(String[] args) {

	}

}
