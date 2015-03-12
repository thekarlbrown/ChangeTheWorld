<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$username=$_GET["username"];
	$username = stripslashes($username);
	
	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];	
	
	$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE iduser='$userid' ORDER BY ididea DESC");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$data_array=$data->fetchAll();
	echo "[";
	$count=count($data_array)-1;
	for($i=0;$i<$count+1;$i++){
		echo json_encode($data_array[$i]);
		if($i<$count){
			echo",";
		}
	}
	echo "]";
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>