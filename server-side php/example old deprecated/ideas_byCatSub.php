<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$cat=$_GET["cat"];
	$sub=$_GET["sub"];
	$cat = stripslashes($cat);
	$sub = stripslashes($sub);
	echo "<br>cat is: " . $cat . " sub is: " . $sub . "<br>";
	$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE cat='$cat' AND sub='$sub' ORDER BY ididea DESC");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	foreach($data as $row){
		echo json_encode($row);
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>