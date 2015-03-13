<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$username=$_GET["username"];
$password=$_GET["password"];
$username = stripslashes($username);
$password=stripslashes($password);

$options = [
    'cost' => 12,
];
$data = $conn->query("Select hash FROM user WHERE author='$username'");
$data->setFetchMode(PDO::FETCH_ASSOC);
$hash_array=$data->fetchAll();
if(password_verify($password,$hash_array[0]['hash'])){
	echo "[{\"verified\":\"1\"}]";
}else{
	echo "[{\"verified\":\"0\"}]";
}

} catch(PDOException $e) {
    //echo 'ERROR: ' . $e->getMessage();
}
?>