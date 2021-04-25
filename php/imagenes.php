<?php
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xnlebena001"; #el usuario para esa base de datos
$DB_PASS="0xzLJgFPqz"; #la clave para ese usuario
$DB_DATABASE="Xnlebena001_MessyWords";

$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
$nombre = $_POST["nombre"];
$nombre = $_POST["foto"];
echo $nombre;

	$resultado = mysqli_query($con, "INSERT INTO `ImagenesUsuarios` (`nombre`, `foto`) VALUES ('$nombre', '$foto')");
		if (!$resultado) {
			echo 'Ha ocurrido algún error: ' . mysqli_error($con);
		}
		else{
			echo 'Se ha insertado el usuario correctamente';
		}
	
?>