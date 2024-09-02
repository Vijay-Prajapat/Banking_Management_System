package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.*;
public class Accounts {
    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection connection , Scanner scanner)
    {
        this.connection=connection;
        this.scanner=scanner;
    }
    public long open_account(String email)
    {
        if(!accountexists(email))
        {
            String open_account_query="INSERT INTO accounts(account_number ,full_name ,email, balance , security_pin)values(?,?,?,?,?)";
          scanner.nextLine();
            System.out.print("Enter your Full Name : ");
            String full_name= scanner.nextLine();
            System.out.print("Enter Initial Amount : ");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter 4 Digit Security Pin: ");
            String security_pin=scanner.nextLine();
            try
            {
                long account_number=generateAccountNumber();
                PreparedStatement pstmt=connection.prepareStatement(open_account_query);
                pstmt.setLong(1,account_number);
                pstmt.setString(2,full_name);
                pstmt.setString(3,email);
                pstmt.setDouble(4,balance);
                pstmt.setString(5,security_pin);
                int rowsaffected=pstmt.executeUpdate();
                if(rowsaffected>0)
                {
                    return account_number;
                }
                else
                {
                    throw new RuntimeException("Account Creation Failed !");
                }

            } catch (SQLException e) {
             e.printStackTrace();
            }


        }
        throw new RuntimeException("Account Creation Failed !");
    }

    public long getAccountNumber(String email)
    {
         String query="Select account_number from accounts where email=?";
         try
         {
             PreparedStatement pstmt=connection.prepareStatement(query);
             pstmt.setString(1,email);
             ResultSet rs= pstmt.executeQuery();
             if(rs.next())
             {
                 return rs.getLong("account_number");
             }
         }
         catch (SQLException e)
         {
             e.printStackTrace();
         }
         throw new RuntimeException("Account Number Doesn't Exist !");
    }
    private long generateAccountNumber()
    {
            try
            {
                Statement stmt=connection.createStatement();
                ResultSet rs=stmt.executeQuery("Select account_number from accounts order by account_number desc limit 1");
                if(rs.next())
                {
                    long last_account_number=rs.getLong("account_number");
                    return last_account_number+1;
                }
                else {
                    return 10000100;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        return 10000100;
    }
    public boolean accountexists(String email)
    {
       String query ="SELECT account_number from accounts where email= ?";
       try
       {
           PreparedStatement pstmt=connection.prepareStatement(query);
           pstmt.setString(1,email);
           ResultSet rs=pstmt.executeQuery();
           if(rs.next())
           {
               return true;
           }
           else {
               return false;
           }
       } catch (SQLException e) {
          e.printStackTrace();
       }

       return false;
    }
}
