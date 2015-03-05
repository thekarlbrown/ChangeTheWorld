<?php
$servername = "localhost";
$username = "root";
$password = "CENSORED";
$db = "mydb";
// Create connection
$conn = new mysqli($servername, $username, $password,$db);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
echo "Connected successfully";
$username=$_GET["username"];
$phone=$_GET["phone"];
$username = stripslashes($username);
$phone = stripslashes($phone);
//$username = mysql_real_escape_string($username);
//$phone = mysql_real_escape_string($phone);
echo "<BR> your username is " . $username . " and your password is " . $phone;
$sql="INSERT IGNORE INTO user (author, phone) VALUES ('$username','$phone')";
if(mysqli_query($conn,$sql))
{
echo "<br>values have been INSERT IGNORE INTO successfully";
}else
{
echo "<br>spaghetti";
}
?>