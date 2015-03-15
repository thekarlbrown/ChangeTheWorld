<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb;charset=utf8','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$search=$_GET["search"];

	$data = $conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE title LIKE '%$search%'	OR descrip LIKE '%:search%' ORDER BY ididea DESC");	
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$data->execute();
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