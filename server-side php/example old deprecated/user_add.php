<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
echo "Connected Successfully";
$username=$_GET["username"];
$phone=$_GET["phone"];
$username = stripslashes($username);
$phone = stripslashes($phone);
$stmt=$conn->prepare("INSERT IGNORE INTO user (author, phone) VALUES ('$username','$phone')");
if($stmt->execute())
{
	echo "<br>values have been INSERT IGNORE INTO successfully";
}else
{
echo "<br>user insertion fails";
}
} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage();
}
?>