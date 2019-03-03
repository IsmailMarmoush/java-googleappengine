var poolIt = function() {
	$.ajax({
		type : "GET",
		url : "/rest/chat/" + $("#nick").val(),
		dataType : "text",
		success : function(message) {
			// The message is added to the <li/> element when it is received.
			$("ul").append("<li>" + message + "</li>");
			poolIt(); // Link to the re-polling when a message is consumed.
		},
		error : function() {
			var conf = confirm("error happened want to try again ?");
			if (conf == true)
				poolIt(); // Start re-polling if an error occurs.
		}
	});
}

// When the submit button is clicked;
$("button").click(function() {

	if ($(".nick").css("visibility") === "visible") { // If <tr> line is
														// visible;
		$("textarea").prop("disabled", false); // Able to enter data
		$(".nick").css("visibility", "hidden"); // Make <tr> line invisible;
		$("span").html("Chat started.."); // Information message

		// Polling operation must be initiated at a time
		poolIt();
	} else
		// if it is not the first time ;
		$.ajax({
			type : "POST", // HTTP POST request
			url : "/rest/chat/" + $("#nick").val(),// access to the
								// sendMessage(..) method.
			dataType : "text", // Incoming data type -> text
			data : $("textarea").val(), // Chat message to send
			contentType : "text/plain", // The type of the sent message
			success : function(data) {

				$("span").html(data); // It writes [Message is sent..] if
										// successful.

				// Blink effect
				$("span").fadeOut('fast', function() {
					$(this).fadeIn('fast');
				});
			}
		});
});