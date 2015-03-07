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
$title=$_GET["title"];
$username=$_GET["username"];
$descrip=$_GET["descrip"];
$userid=$_GET["userid"];
$cat=$_GET["cat"];
$subcat=$_GET["subcat"];
$title= stripslashes($title);
$username= stripslashes($username);
$descrip= stripslashes($title);
$userid= stripslashes($userid);
$cat= stripslashes($cat);
$subcat= stripslashes($subcat);
$ideaid="SELCT 'user' FROM 'user' WHERE author='$username'";
//$ideainc ="SELECT 'AUTO_INCREMENT' FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '$mydb' AND   TABLE_NAME   = 'user'"
echo "<BR> your title is " . $title . ",username is " . $username . ",descrip is " . $descrip . ",userid is " . $userid . ",cat is " . $cat . ",subcat is ". $subcat;
echo "<br> query: " . mysqli_query($conn,$ideaid);
/*$sql="INSERT IGNORE INTO user (author, phone) VALUES ('$username','$phone')";
if(mysqli_query($conn,$sql))
{
echo "<br>values have been INSERT IGNORE INTO successfully";
}else
{
echo "<br>spaghetti";
}
*/
?>