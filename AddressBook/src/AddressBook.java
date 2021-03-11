import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AddressBook {
	
	
	public static final String DELIMITER = "~";
	private static ArrayList<Contact> addressBook = new ArrayList<>();
	static Scanner sc = new Scanner(System.in);


    private static void createFile() {
        try {
            File myFile = new File("AddressBook.txt");
            if(myFile.createNewFile()) {
                System.out.println();
                System.out.println("Welcome to this address book program!");
                System.out.println("** New address book created for you **");
            } else {
                loadAddressBook();
                System.out.println();
                System.out.println("Welcome back to your address book!");
                System.out.println("** Loaded address book from file **");
            }
        } catch(IOException ioE) {
            System.out.println();
            System.out.println("Error creating file");
            System.out.println(ioE.getMessage());
        }
    }

    // used on end to store all values from the addressBook array list in the file
    // will overwrite everything previously stored in the file
    private static void storeAddressBook() throws IOException {
        Collections.sort(addressBook);
        FileWriter fw = new FileWriter("AddressBook.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.write("\n");
        for(Contact cont : addressBook) {
            pw.write(cont.getFirstName() + DELIMITER);
            pw.write(cont.getLastName() + DELIMITER);
            pw.write(cont.getAddress() + DELIMITER);
            pw.write(cont.getPhoneNumber() + "");
            pw.write("\n");
        }
        pw.close();
    }

    // used on start (assuming the address book already exists) to get all contacts from the file and store them in the addressBook array list
    private static void loadAddressBook() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("AddressBook.txt"));
            String line = "";
            br.readLine();

            while((line = br.readLine()) != null) {
                String[] contactDetails = line.split(DELIMITER);
                if(contactDetails.length > 0) {
                    Contact loadContact = new Contact(contactDetails[0], contactDetails[1], contactDetails[2], contactDetails[3]);
                    addressBook.add(loadContact);
                }
            }
        } catch (Exception e) {
            System.out.println("here");
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ioE) {
                System.out.println("Error occurred while closing the Buffered Reader");
                ioE.printStackTrace();
            }
        }
    }
	
	// returns index of desired contact
		public int search() throws IOException {
			
			if(addressBook.isEmpty()) throw new IOException("Address Book is EMPTY\n");
			
			System.out.println("This search will return the first instance of the search parameter inputed.\n"
					+ "If this is the wrong contact, please try again.");
			
			String willCheckCurrent;
			int index = -1;
			String number;
			try {
				System.out.println(" To check current list for reference, enter any key. Otherwise, enter 'no'. ");			
				willCheckCurrent = sc.nextLine();
			
				if(willCheckCurrent.equals("no")) {/* do nothing */}
				else {
					System.out.println(willCheckCurrent);
					listAddressBook(); //display the current address book for reference
				}
			}catch(InputMismatchException e) {System.out.println(e);}	
				boolean searchPending = true;
				while(searchPending) {
					try {
						System.out.println("Please enter a number (1-3) to choose how you would like to search:\n"
								+ "1. First Name\n"
								+ "2. Last Name\n"
								+ "3. Phone Number\n");
					
					String searchType = sc.nextLine();
					int convertedToInt = Integer.parseInt(searchType);
					switch(convertedToInt) {
					
					// search by first name
					case 1:
						System.out.println("Please enter the FIRST name of the person you would like to find");
						String fnSearch = sc.nextLine();
						for(Contact c : addressBook) {
							if(c.getFirstName().equalsIgnoreCase(fnSearch)) {
								index = addressBook.indexOf(c);
								System.out.println(index + " is the index");
								c.toString();
								break;
								}
						}
						searchPending= false;
						break;
						
					// search by last name	
					case 2: 
						System.out.println("Please enter the LAST name of the person you would like to find");
						String lnSearch = sc.nextLine();
						for(Contact c : addressBook) {
							if(c.getLastName().equalsIgnoreCase(lnSearch)) {
								index = addressBook.indexOf(c);
								c.toString();
								break;
								}
						};
						searchPending= false;
						break;
						
					// search by phone number	
					case 3: 
						System.out.println("Please enter the 10 digit phone number of the person you would like to search");
						number = sc.nextLine();
						if (isValidPhoneNumber(number)) {
							for(Contact c: addressBook) {
								if (c.getPhoneNumber().equalsIgnoreCase(number)) {
									index = addressBook.indexOf(c);
									c.toString();
									break;
								}
							}
						}
						searchPending= false;
						break;
						
					// invalid input	
					//default:
						//throw new IOException("That was an invalid input");
					}
					
					
					}catch(InputMismatchException ioe){
						System.out.println(ioe);
					}catch(NumberFormatException e) {
						System.out.println(e+"\nThat was an invalid input");
					}
				}
				System.out.println(addressBook.get(index).toString());
				if(searchVerification()==true) return index;
				else {
					System.out.println("You can try searching with another parameter next time");
					return -1;
				}
		}
		
		// checks to see if the search was successful
		public boolean searchVerification() throws IOException{
			boolean verificationPending = true;
			System.out.println("Was this the contact you were searching for? (yes/no)");
			String yesOrNo;
			try {
				while(verificationPending) {	
					yesOrNo = sc.nextLine();
					if(yesOrNo.equals("no") || yesOrNo.equals("No")){
						System.out.println("Incorrect input was chosen");
						return false;
					}else if(yesOrNo.equals("yes") || yesOrNo.equals("Yes")) {
						System.out.println("Correct input was chosen");
						return true;
					}else {
						System.out.println(yesOrNo);
						throw new IOException("That was an invalid responce, try again (yes/no)");
					}
				}
			}catch(IOException e) {System.out.println(e);}
			System.out.println("something happened in searchVerification, returning true");
			return true;
		}
		
		// checks to see if phone number is valid
		public boolean isValidPhoneNumber(String phoneNum) {
			try {
				@SuppressWarnings("unused")
				int convertedToInt;
				convertedToInt = Integer.parseInt(phoneNum);
			}catch(NumberFormatException nfe) {
				System.out.println(nfe + "\nMake sure to only include the 10 digit number ONLY. "
						+ "\nNo dashes or any other extra characters.");
				return false;
			}
			if(phoneNum.length() != 10) {
				System.out.println("Invalid number entered. Please make sure to include only "
						+ "the 10 digit phonenumber");
				return false;
			}
			return true;
		}
		
		// adds a contact to address book
		public void addContact() {
			try {
				String firstName_, lastName_, address_, phonenumber_;
				
				System.out.println("Please enter the Contact's first name");
				firstName_ = sc.nextLine();
				
				System.out.println("Please enter the Contact's last name");
				lastName_ = sc.nextLine();
				
				System.out.println("Please enter the Contact's address");
				address_ = sc.nextLine();
				
				System.out.println("Please enter the Contact's 10 digit phone number");
				phonenumber_ = sc.nextLine();
				if(phonenumber_.length() != 10) {
					throw new IOException("This is not a valid phone number.\n"
							+ "Please make sure to use a 10 digit phone number without any extra characters\n"
							+ "e.g. 1234567890 NOT 123-456-7890");
				}
				addressBook.add(new Contact(firstName_, lastName_, address_, phonenumber_));
			}catch(IOException ioe){
				System.out.println(ioe);
				}
		}
		
		// updates address
		public void updateContact() throws IOException {
			int index = search();
			if (index == -1) {
				System.out.println("Index not found. Exiting update");
				return;
			}
			boolean continueUpdate = true;
			while(continueUpdate) {
				System.out.println("Enter...\n"
					+ "1 for first name\n"
					+ "2 for last name\n"
					+ "3 for address\n"
					+ "4 for phone number\n"
					+ "or any other key to quit update");
				try {
					String updateNumber = sc.nextLine();
					
					
					switch(updateNumber) {
						case("1"):
							System.out.println("Please enter the new first name for this contact");
						addressBook.get(index).setFirstName(sc.nextLine());
							break;
						case("2"):
							System.out.println("Please enter the new last name for this contact");
						addressBook.get(index).setLastName(sc.nextLine());
							break;
						case("3"):
							System.out.println("Please enter the new address for this contact");
						addressBook.get(index).setAddress(sc.nextLine());
							break;
						case("4"):
							System.out.println("Please enter the new phone number for this contact");
						addressBook.get(index).setPhoneNumber(sc.nextLine());
							break;
						default:
							continueUpdate = false;
							break;
						}
					
					//br.close();
				}catch(InputMismatchException ioe) {
					System.out.println(ioe);
					return;
				}
			}
		}	

		
		// delete a specified contact
		public void deleteContact() throws IOException {
			if(addressBook.isEmpty()) {
				System.out.println("What are you doing here? It's already empty!");
				return;
			}
			try {
				int index = search();
				if (index == -1) {
					System.out.println("Index not found. Exiting deletion");
					return;
				}
				addressBook.remove(addressBook.get(index)); // removes contact at the index found by the search method
				System.out.println("Successfully deleted contact");
			} catch (IOException e) {
				System.out.println(e);
			} catch(Exception e) {System.out.println(e);}
		}
		
		public void listAddressBook() {
			if (addressBook.isEmpty()) {
				System.out.println("Address Book Empty");
				return;
			}
			 Collections.sort(addressBook); 
			for (Contact c : addressBook) {
				System.out.println(c.toString());
			}
		}
		
		
		
		public void mainMenu(AddressBook ab) {
			try {
				boolean continueProgram = true;
				System.out.println("Address Book\n"
						+ "___________\n"
						+ "Type in a number (1-7) corresponding to a command listed below to do things\n");
				while(continueProgram) {	
					try {
						System.out.println(""
								+ "1   Add a New Contact\n"
								+ "2   Update an Existing Contact\n"
								+ "3   Delete a Contact\n"
								+ "4   Search for a contact by last name\n"
								+ "5   Save Address Book\n"
								+ "6   List all Contacts\n"
								+ "7   Quit");
						String commandNumber = sc.nextLine();
						
						int convertedToInt = Integer.parseInt(commandNumber); // checks to see if input is a valid input, otherwise catch exception
						
						switch(convertedToInt) {
						case 1: 
							ab.addContact();
							break;
						case 2:
							ab.updateContact();
							break;
						case 3:
							ab.deleteContact();
							break;
						case 4:
							ab.search();
							System.out.println();
							break;
						case 5: 
							storeAddressBook();
							//ab.save();
							break;
						case 6:
							ab.listAddressBook();
							break;
						case 7:
							System.out.println("Quitting program, saving changes");
							storeAddressBook();
							//ab.save();
							continueProgram = false;
							break;
						default:
							throw new IOException("Invalid entry. Please ONLY enter a number (1-6)");
						}
						
					}catch(IOException e) {
						System.out.println(e);
						break;
					}catch(NumberFormatException nfe) {
						System.out.println(nfe + "\nPlease ONLY enter a number (1-6)");
						continue;
					}
				}
			}catch(Exception e) {System.out.println(e);}
		}
	
	//6. Saves items in array list into .CSV file
	public void save() {
		Collections.sort(addressBook);
        FileWriter fw;
		try {
			fw = new FileWriter("AddressBook.csv");
	        PrintWriter pw = new PrintWriter(fw);
	        pw.write("\n");
	        for(Contact cont : addressBook) {
	            pw.write(cont.getFirstName() + DELIMITER);
	            pw.write(cont.getLastName() + DELIMITER);
	            pw.write(cont.getAddress() + DELIMITER);
	            pw.write(cont.getPhoneNumber() + "");
	            pw.write("\n");
	        }
	        pw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		createFile();
		loadAddressBook();
		
		//createFile("AddressBook.csv");
		//readFile("AddressBook.csv");
		boolean proceedWithProgram = true;
		while(proceedWithProgram == true) {
					// populate initial address book for test purpose
					if(addressBook.isEmpty()) {
						addressBook.add(new Contact("Maaz", "Sheikh", "in the computer", "7894561232"));
						addressBook.add(new Contact("Deepa", "Mahalingam", "College of marin", "7894444232"));
						addressBook.add(new Contact("Joe", "Biden", "white house", "1111111111"));
						addressBook.add(new Contact("My", "Cat", "Her bed", "7845129635"));
					}
					AddressBook ab = new AddressBook();
					ab.mainMenu(ab);
					proceedWithProgram = false;
		}
		sc.close();	
	}
}
	
	
