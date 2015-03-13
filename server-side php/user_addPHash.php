<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$username=$_GET["username"];
$email=$_GET["email"];
$password=$_GET["password"];
$username = stripslashes($username);
$email = stripslashes($email);
$password=stripslashes($password);

$options = [
    'cost' => 12,
];
$hash = password_hash($password,PASSWORD_BCRYPT,$options);
$stmt=$conn->prepare("INSERT INTO user (author,hash,email) VALUES ('$username','$hash','$email')");
if($stmt->execute()){
	echo "[{\"created\":\"1\"}]";
}

} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage();
}
?>