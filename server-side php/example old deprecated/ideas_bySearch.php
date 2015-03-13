<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$search=$_GET["search"];
	$search = stripslashes($search);
	echo "<br>search query is: " . $search . "<br>";
	$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE title LIKE '%$search%'	OR descrip LIKE '%$search%' ORDER BY ididea DESC");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	foreach($data as $row){
		echo json_encode($row);
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>