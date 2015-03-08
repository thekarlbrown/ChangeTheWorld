<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$username=$_GET["username"];
	$ideaid=$_GET["ideaid"];
	$case=$_GET["case"];
	$username = stripslashes($username);
	$ideaid=stripslashes($ideaid);
	$case=stripslashes($case);

	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];
	echo "<br> user id is: " . $userid;

	$stmt=$conn->prepare("INSERT IGNORE favratd (iduser, ididea) VALUES ('$userid','$ideaid')");
	if($stmt->execute())
	{
		echo "<br>favorite created or reinvented";
	}else
	{
		echo "<br>user insertion fails";
	}

	if($case==0){
		$stmt=$conn->prepare("UPDATE favratd set fav='0' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute())
		{
			echo "<br>favorite removed";
		}else
		{
			echo "<br>favorite removal fails";
		}

	}else if($case==1){
		$stmt=$conn->prepare("UPDATE favratd set fav='1' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute())
		{
			echo "<br>favorite added";
		}else
		{
			echo "<br>favorite add fails";
		}

	}else if($case==2){
		$stmt=$conn->prepare("UPDATE favratd set rated=NULL WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute())
		{
			echo "<br>rating removed";
		}else
		{
			echo "<br>rating removal fails";
		}
	}else if($case==3){
		$stmt=$conn->prepare("UPDATE favratd set rated='1' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute())
		{
			echo "<br>rated positive";
		}else
		{
			echo "<br>rating positive fails";
		}
	}else if($case==4){
		$stmt=$conn->prepare("UPDATE favratd set rated='0' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute())
		{
			echo "<br>rated negative";
		}else
		{
			echo "<br>rating negative fails";
		}
	}	

} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>