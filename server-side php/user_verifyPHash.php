<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb;charset=utf8','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$password=$_GET["password"];
$username=$_GET["username"];
$password=stripslashes($password);

$options = [
    'cost' => 12,
];
$data = $conn->prepare("SELECT hash FROM user WHERE author=?");

$data->execute(array($username));
$fetch=$data->fetch(PDO::FETCH_ASSOC);
$hash_array=$fetch['hash'];
if(password_verify($password,$hash_array)){
	echo "[{\"verified\":\"1\"}]";
}else{
	echo "[{\"verified\":\"0\"}]";
}

} catch(PDOException $e) {
    //echo 'ERROR: ' . $e->getMessage();
}
?>