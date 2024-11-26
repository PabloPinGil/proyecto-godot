extends Area2D

var speed = 1300
var direction
var damage = 1

func _ready():
	# Obtén el nodo del jugador desde la escena
	var player = get_node("/root/Main/player")  # Ajusta la ruta de acuerdo a tu jerarquía de nodos
	var player_position = player.global_position  # La posición global del jugador
	var cursor_position = get_global_mouse_position()  # La posición global del cursor
	self.body_entered.connect(self._on_body_entered)

	# Calcular la dirección hacia el cursor
	direction = (cursor_position - player_position).normalized()
	$Sprite2D.rotation = direction.angle()


func _physics_process(delta):
	position += direction * speed * delta


func _on_body_entered(body):
	if body.is_in_group("enemy"):
		body.get_hit(damage)
		queue_free()
