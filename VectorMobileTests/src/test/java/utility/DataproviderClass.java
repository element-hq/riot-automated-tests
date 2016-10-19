package utility;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

public class DataproviderClass {
	/**
	 *
	 * @return Object[][] of tests datas
	 * @throws Exception 
	 */
	@DataProvider(name="SearchProvider")
	public static Object[][] getDataFromDataprovider(Method m) throws Exception{
		ExcelUtils.setExcelFile(Constant.Path_TestData + m.getName()+".xlsx","Sheet1");
		int maxRows=ExcelUtils.getNbRows();
		int maxCol=ExcelUtils.getNbCol();
		
		System.out.println("nb rows :"+maxRows);
		System.out.println("nb col :"+maxCol);
		
				Object [][] objArray = new Object[maxRows][];
				//parcourt des lignes
				for(int iNbRows=0;iNbRows<=maxRows-1;iNbRows++){
					//parcourt des colonnes (cellules jdd)
					objArray[iNbRows] = new Object[maxCol];
					for(int iNbCol=0;iNbCol<=maxCol-1;iNbCol++){			
					    objArray[iNbRows][iNbCol] = ExcelUtils.getCellData(iNbRows+1, iNbCol);
						//System.out.println(ExcelUtils.getCellData(iNbRows, iNbCol));
					}
				}
				return objArray;

	}
}
