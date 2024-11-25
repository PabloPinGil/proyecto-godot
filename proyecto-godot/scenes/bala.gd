extends Area2D

var speed = 1000
var direction

func _ready():
	# Obtén el nodo del jugador desde la escena
	var player = get_node("/root/Node2D/player")  # Ajusta la ruta de acuerdo a tu jerarquía de nodos
	var player_position = player.global_position  # La posición global del jugador
	var cursor_position = get_global_mouse_position()  # La posición global del cursor

	# Calcular la dirección hacia el cursor
	direction = (cursor_position - player_position).normalized()


func _physics_process(delta):
	position += direction * speed * delta


func _on_Bullet_body_entered(body):
	if body.is_in_group("mobs"):
		body.queue_free()
	queue_free()
