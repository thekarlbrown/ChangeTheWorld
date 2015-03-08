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
	if($stmt->execute()){
		echo "<br>favorite created or reinvented";
	}else{
		echo "<br>user insertion fails";
	}

	if($case==0){
		$stmt=$conn->prepare("UPDATE favratd set fav='0' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute()){
			echo "<br>favorite removed";
		}else{
			echo "<br>favorite removal fails";
		}
		

	}else if($case==1){
		$stmt=$conn->prepare("UPDATE favratd set fav='1' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute()){
			echo "<br>favorite added";
		}else{
			echo "<br>favorite add fails";
		}

	}else if($case==2){
		$query=$conn->query("SELECT rated FROM favratd WHERE iduser='$userid' AND ididea='$ideaid'");
		$fetch=$query->fetch();
		$lastrated=$fetch['rated'];
		echo "<br> previous value " . $lastrated;
		$stmt=$conn->prepare("UPDATE favratd set rated=NULL WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute()){
			echo "<br>attempt to remove rating";
			if($lastrated==NULL){
				echo "<br> previously null, no change necessary";
			}else if($lastrated==0){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown-1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br>ideas updated thumps down -1";
				}else{
					echo "<br> ideas failed to reflect change thumps down -1";
				}
			}else if($lastrated==1){
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup-1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br> ideas updated thumbsup-1";
				}else{
					echo "<br> ideas failed to reflect change thumbsup-1";
				}
			}
		}else{
			echo "<br>attempted rating removal fails";
		}
		
	}else if($case==3){
		$query=$conn->query("SELECT rated FROM favratd WHERE iduser='$userid' AND ididea='$ideaid'");
		$fetch=$query->fetch();
		$lastrated=$fetch['rated'];
		echo "<br> previous value " . $lastrated;
		$stmt=$conn->prepare("UPDATE favratd set rated='1' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute()){
			echo "<br>attempt to rate positive";
			if($lastrated==NULL){
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup+1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br> ideas updated thumbsup+1";
				}else{
					echo "<br> ideas failed to reflect change thumbsup+1";
				}
			}else if($lastrated==0){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown-1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br>ideas updated thumps down -1";
				}else{
					echo "<br> ideas failed to reflect change thumps down -1";
				}
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup+1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br> ideas updated thumbsup+1";
				}else{
					echo "<br> ideas failed to reflect change thumbsup+1";
				}
			}else if($lastrated==1){
				echo "<br> previously positive, no change necessary";
			}
		}else{
			echo "<br>attempted rating positive fails";
		}
		
	}else if($case==4){
		$query=$conn->query("SELECT rated FROM favratd WHERE iduser='$userid' AND ididea='$ideaid'");
		$fetch=$query->fetch();
		$lastrated=$fetch['rated'];
		echo "<br> previous value " . $lastrated;
		$stmt=$conn->prepare("UPDATE favratd set rated='0' WHERE iduser='$userid' AND ididea='$ideaid'");
		if($stmt->execute()){
			echo "<br>attempt to rate negative";
			if($lastrated==NULL){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown+1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br> ideas updated thumbsdown+1";
				}else{
					echo "<br> ideas failed to reflect change thumbsdown+1";
				}
			}else if($lastrated==0){
				echo "<br> previously negative, no change necessary";
			}else if($lastrated==1){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown+1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br> ideas updated thumbsdown+1";
				}else{
					echo "<br> ideas failed to reflect change thumbsdown+1";
				}
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup-1 WHERE ididea='$ideaid'");
				if($stmt->execute()){
					echo"<br> ideas updated thumbsup-1";
				}else{
					echo "<br> ideas failed to reflect change thumbsup-1";
				}
			}
			
		}else{
			echo "<br>attempted rating negative fails";
		}
	}	

} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>