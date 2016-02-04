# ItemStackSerialization
This is a simple static library to serialize an itemstack into a byte array and back.

---

Use this as you see fit but do link people to this repository when sharing.

---
General info
-
Average items will give a byte array with a size less than 128 bytes. But there are some exceptions that might go over that size like big fireworks and custom potions. Books and heads with player data WILL go over that, books especialy will take alot of data depening on what is writen in them.

---
Usage:
-
This is the basic use of the library.

//your itemstack 

ItemStack item = new ItemStack(Material.DIAMOND_SWORD);

//serializing the item

byte[] data = ItemStackSerialization.serialize(item);

//deserializing the data

ItemStack item = ItemStackSerialization.deserialize(data);

