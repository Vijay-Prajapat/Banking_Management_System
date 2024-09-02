package banking_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.jar.JarOutputStream;

public class AccountManager {

    private Connection connection;
    private Scanner scanner;
    AccountManager(Connection conncetion , Scanner scanner)
    {
        this.connection=conncetion;
        this.scanner=scanner;
    }

    public void credit_money(long account_number)throws SQLException
    {
        scanner.nextLine();
        System.out.println("Enter Amount : ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin : ");
        String security_pin=scanner.nextLine();

        try
        {
            connection.setAutoCommit(false);
            if(account_number!=0)
            {
                PreparedStatement pstmt=connection.prepareStatement("select * from accounts where account_number = ?  and security_pin = ? ");
                pstmt.setLong(1,account_number);
                pstmt.setString(2,security_pin);
                ResultSet rs= pstmt.executeQuery();
                if(rs.next())
                {
                    String credit_query="update accounts set balance =balance + ? where account_number = ? ";
                    PreparedStatement pstmt1=connection.prepareStatement(credit_query);
                    pstmt1.setDouble(1,amount);
                    pstmt1.setLong(2,account_number);
                    int rowsaffected=pstmt1.executeUpdate();
                    if(rowsaffected>0)
                    {
                        System.out.println("Rs. "+amount+" credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }
                    else
                    {
                        System.out.println("Invalid Security Pin !");
                    }
                }
            }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void debit_money(long account_number)throws SQLException
    {
        scanner.nextLine();
        System.out.println("Enter Amount :");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin : ");
        String security_pin= scanner.nextLine();
        try
        {
            connection.setAutoCommit(false);
            if(account_number!=0)
            {
                PreparedStatement pstmt=connection.prepareStatement("select*from accounts where account_number = ? and security_pin=?");
                pstmt.setLong(1,account_number);
                pstmt.setString(2,security_pin);
                ResultSet rs=pstmt.executeQuery();
                if(rs.next())
                {
                    double current_balance=rs.getDouble("balance");
                    if(amount<=current_balance)
                    {
                        String debit_query="update accounts set balance = balance - ? where account_number = ?";
                        PreparedStatement pstmt1=connection.prepareStatement(debit_query);
                        pstmt1.setDouble(1,amount);
                        pstmt1.setLong(2,account_number);
                        int rowsaffected = pstmt1.executeUpdate();
                        if(rowsaffected > 0)
                        {
                            System.out.println("Rs. "+ amount + " debited Successfully ");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else
                        {
                            System.out.println("Transaction Failed !");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else
                    {
                        System.out.println("Insufficient Balance !");
                    }
                }
                else {
                    System.out.println("Invalid Pin !");
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);

    }


    public void transfer_money(long sender_account_number)throws SQLException
    {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number=scanner.nextLong();
        System.out.print("Enter Amount : ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin=scanner.nextLine();

        try
        {
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number !=0)
            {
                PreparedStatement pstmt= connection.prepareStatement("Select * from accounts where account_number = ? and security_pin = ?");
                pstmt.setLong(1,sender_account_number);
                pstmt.setString(2,security_pin);
                ResultSet rs=pstmt.executeQuery();
                if(rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update accounts set balance =balance- ? where account_number=?";
                        String credit_query = "update accounts set balance=balance +  ? where account_number=?";

                        PreparedStatement pstmtc = connection.prepareStatement(credit_query);
                        PreparedStatement pstmtd = connection.prepareStatement(debit_query);

                        pstmtc.setDouble(1, amount);
                        pstmtc.setLong(2, receiver_account_number);
                        pstmtd.setDouble(1, amount);
                        pstmtd.setLong(2, sender_account_number);

                        int rowsaffected1 = pstmtd.executeUpdate();
                        int rowsaffected2 = pstmtc.executeUpdate();
                        if (rowsaffected1 > 0 && rowsaffected2 > 0) {
                            System.out.println("Transaction Successful !!");
                            System.out.println("Rs. " + amount + " Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed !!");
                            connection.rollback();
                            ;
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance !");
                    }
                }else {
                    System.out.println("Invalid security pin !");
                }
            }
            else {
                System.out.println("Invalid account number !");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        connection.setAutoCommit(true);
    }


    public void getBalance(long account_number)
    {
        scanner.nextLine();
        System.out.println("Enter Security pin : ");
        String security_pin=scanner.nextLine();

        try
        {
            PreparedStatement pstmt=connection.prepareStatement("select balance from accounts where account_number=? and security_pin = ?");
            pstmt.setLong(1,account_number);
            pstmt.setString(2,security_pin);
            ResultSet rs=pstmt.executeQuery();
           if(rs.next())
           {
               double balance=rs.getDouble("balance");
               System.out.println("Balance : "+balance);
           }
           else {
               System.out.println("Invalid Pin !");
           }
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }
}
