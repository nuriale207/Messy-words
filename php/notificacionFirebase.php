<?php
 // Cargamos los datos de la notificacion en un Array
    $notification = array();
    $notification['title'] = 'Título de la notificación';
    $notification['body'] = 'Ejemplo de notificación firebase';
    $notification['image'] = '';
    $notification['action'] = '';
    $notification['action_destination'] = '';            
    $topic = "topic_general";

	
    $fields = array(
        'to' => '/topics/' . $topic,
        'data' => $notification,
    );
    // Set POST variables
    $url = 'https://fcm.googleapis.com/fcm/send';
    $headers = array(
                'Authorization: key=AAAA1uEfr3c:APA91bE4Cyj39tvSrKiicH6sGemqqHq1cZd3Lwec30yC5yDxkbx265AMXVHpRh7WticaXHNhWY8cB_NpCVwQi5ukmdivpPFL6RvHWULosAbSRGqckvoos3uLcvEAM_s1tTByZx2VEjd1',
                'Content-Type: application/json'
                );
                
    // Open connection
    $ch = curl_init();
    // Set the url, number of POST vars, POST data
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    // Disabling SSL Certificate support temporarily
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));       
    
    $result = curl_exec($ch);
    if($result === FALSE) {
        die('Curl failed: ' . curl_error($ch));
    }
    // Close connection
    curl_close($ch);

?>