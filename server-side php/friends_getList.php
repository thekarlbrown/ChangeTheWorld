<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$username=$_GET["username"];
	$username = stripslashes($username);
	
	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];
	echo "<br> user id is: " . $userid . "<br>";
	
	$data = $conn->query("SELECT idfriend FROM friends WHERE iduser='$userid'");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	foreach($data as $row){
		$r[]=json_encode($row);
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>
<script type="text/javascript">
var jArray = <?php echo json_encode($r); ?>;
function show(j) {
    for(var i=0; i < j.length; i++){ document.write(j[i]); }
};
show(jArray);
</script>

