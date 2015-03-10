<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','sqlDEB123');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$username=$_GET["username"];
	$username = stripslashes($username);
	
	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];
	echo "<br> user id is: " . $userid . "<br>";
	
	$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub FROM ideas LEFT JOIN favratd ON ideas.ididea=favratd.ididea WHERE favratd.iduser='$userid'");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	foreach($data as $row){
		echo json_encode($row);
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>
