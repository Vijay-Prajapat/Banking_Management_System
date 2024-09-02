package banking_system;
import java.sql.*;
import java.util.*;

public class User {
    private Connection connection;
    private Scanner scanner;
    public User(Connection conncetion , Scanner scanner)
    {
        this.connection=conncetion;
        this.scanner=scanner;
    }
    public void register()
    {
        scanner.nextLine();
        System.out.print("Full Name : ");
        String full_name=scanner.nextLine();
        System.out.print("Email : ");
        String email=scanner.nextLine();
        System.out.print("Password : ");
        String password=scanner.nextLine();

        if(user_exists(email))
        {
            System.out.println("user Already Exists for this Email Address !.");
            return;
        }
        String register_query=" INSERT INTO USER (full_name , email , password ) values (? ,? ,?)";
        try
        {
            PreparedStatement pstmt=connection.prepareStatement(register_query);
            pstmt.setString(1,full_name);
            pstmt.setString(2,email);
            pstmt.setString(3,password);
           int affectedrows=pstmt.executeUpdate();
           if(affectedrows>0)
           {
               System.out.println("Registration Successful ! ");
           }
           else
           {
               System.out.println("Registration Failed !" );
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String login()
    {
        scanner.nextLine();
        System.out.println("Email : ");
        String email=scanner.nextLine();
        System.out.println("Password : ");
        String password=scanner.nextLine();

        String login_query="SELECT*FROM user WHERE email = ?  and password= ?";
        try
        {
            PreparedStatement pstmt= connection.prepareStatement(login_query);
            pstmt.setString(1,email);
            pstmt.setString(2,password);
            ResultSet rs= pstmt.executeQuery();
            if(rs.next())
            {
                return email;
            }
            else
            {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public boolean user_exists(String email)
    {
        String query="SELECT * FROM user WHERE email= ?";
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
