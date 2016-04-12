import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

// INDICES BPLUSTREE SHOWIYET RECOREDS, TO BUILD INDICES

//ARRAYLIST OF PAGES GHEER ARRAYLIST OF STRING ELY HEYA DIRCTERIOS 
public class Table {
	static ArrayList<Page> table;
	static int pageNumber;
	static boolean start;
	static ObjectOutputStream oos;
	String name;

	public Table(String name) {

		table = new ArrayList<Page>();
		pageNumber = 0;
		start = true;
		this.name = name;

	}

	public static void insert(Hashtable<String, String> htblColNameValue,
			String strTableName) throws FileNotFoundException, IOException {
		if (!start) {
			Page page = new Page();
			table.add(page);
			start = false;
			oos = new ObjectOutputStream(new FileOutputStream(new File(
					DBApp.dataDir + strTableName + "p" + pageNumber + ".ser")));

		}
		if (table.get(pageNumber).isFull()) {
			pageNumber++;
			Page page = new Page();
			table.add(page);
			oos = new ObjectOutputStream(new FileOutputStream(new File(
					DBApp.dataDir + strTableName + "p" + pageNumber + ".ser")));

		}
		table.get(pageNumber).insert(htblColNameValue);
		oos.writeObject(htblColNameValue);
		oos.close();
	}

	public ArrayList<Hashtable<String, String>> search(
			Hashtable<String, String> htblColNameValue) {
		ArrayList<Hashtable<String, String>> result = new ArrayList<Hashtable<String, String>>();
		for (int i = 0; i < table.size(); i++) {
			ArrayList<Hashtable<String, String>> searchInPage = table.get(i)
					.search(htblColNameValue);
			result.addAll(searchInPage);

		}
		return result;
	}

	public void delete(Hashtable<String, String> htblColNameValue) {
		ArrayList<Hashtable<String, String>> deleteList = search(htblColNameValue);
		deleteList.remove(htblColNameValue);

	}

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws IOException, DBAppException {

		ObjectOutputStream meta = null;
		ObjectOutputStream table = null;
		File f = null;
		try {
			f = new File(DBApp.dataDir + strTableName);
			f.mkdir();
			meta = new ObjectOutputStream(new FileOutputStream(DBApp.dataDir
					+ strTableName + "/metadata.ser"));
			table = new ObjectOutputStream(new FileOutputStream(
					f.getAbsolutePath() + "/" + strTableName + ".ser"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}

		Iterator<Entry<String, String>> colType = htblColNameType.entrySet()
				.iterator();
		Entry<String, String> type;
		String row;
		while (colType.hasNext()) {
			row = "";
			type = colType.next();
			row = strTableName + "," + type.getKey() + "," + type.getValue()
					+ "," + type.getKey().equals(strKeyColName) + ","
					+ type.getKey().equals(strKeyColName) + ","
					+ htblColNameRefs.get(type.getKey()) + "\n";
			meta.writeBytes(row);
		}
		table.close();
		meta.close();
	}

	public ArrayList<Hashtable<String, String>> searchbyname(String columnName) {
		ArrayList<Hashtable<String, String>> result = null;
		for (int i = 0; i < table.size(); i++) {
			result.add(table.get(i).searchbyname(columnName, i));
		}
		return result;
	}
}
