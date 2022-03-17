package fr.edencraft.edenitems.content;

public class ItemsContent {

	public static String CONTENT = """
			# 'moon_pickaxe' is just an id.
			'moon_pickaxe':
			  # Material of the item you want to select.
			  material: DIAMOND_PICKAXE
			  # model_id means Custom_Model_Data of the item. If you don't want to specify a
			  # custom model data, you can comment the line below.
			  model_id: 10010
			  # All listed commands below will be canceled only if player hold the item in MAIN_HAND or OFF_HAND
			  # To ignore this set to an empty list like that:
			  # blocked_commands_in_hands: []
			  # Then remove the block below.
			  blocked_commands_in_hands:
			    "/repair":
			      message: "&dEden&eCraft &f&l» &#9000ffVous ne pouvez pas réparer un outil Lunaire avec le /repair ! 
			        &8(Utilisez une gemme Lunaire)"
			    "/repair hand":
			      message: "&dEden&eCraft &f&l» &#9000ffVous ne pouvez pas réparer un outil Lunaire avec le /repair hand 
			        ! &8(Utilisez une gemme Lunaire)"
			    /repair all:
			      message: "&dEden&eCraft &f&l» &#9000ffLa commande repairall a été annulé car vous avez un item Luanire
			        dans votre main !"
			  # All listed commands below will be canceled if player has the item in his inventory.
			  # To ignore this set to an empty list like that:
			  # blocked_commands_in_inventory: []
			  # Then remove the block below.
			  blocked_commands_in_inventory:
			    "/repair all":
			      message: "&dEden&eCraft &f&l» &#9000ffLa commande repairall a été annulé car un item Lunaire a été 
			        detecté dans votre inventaire."
			""";

}
