<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$username=$_GET["username"];
$email=$_GET["email"];
$password=$_GET["password"];

$password=stripslashes($password);

$options = [
    'cost' => 12,
];
$hash = password_hash($password,PASSWORD_BCRYPT,$options);
$stmt=$conn->prepare("INSERT INTO user (author,hash,email) VALUES (:username,'$hash',:email)");
$stmt->bindValue(':username',$username,PDO::PARAM_STR);
$stmt->bindValue(':email',$email,PDO::PARAM_STR);
if($stmt->execute()){
	echo "[{\"created\":\"1\"}]";
}else{
	echo "[{\"created\":\"0\"}]";
}

} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage();
}
?>
