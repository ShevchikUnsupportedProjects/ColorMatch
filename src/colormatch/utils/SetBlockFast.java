/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package colormatch.utils;

import net.minecraft.server.v1_7_R3.Block;
import net.minecraft.server.v1_7_R3.Chunk;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;

public class SetBlockFast {

	@SuppressWarnings("deprecation")
	public static void setBlock(World world, int x, int y, int z, int blockId, int data) {
		try {
			net.minecraft.server.v1_7_R3.World w = ((CraftWorld) world).getHandle();
			Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
			chunk.a(x & 0x0f, y, z & 0x0f, Block.e(blockId), data);
			w.notify(x, y, z);
		} catch (Throwable t) {
			world.getBlockAt(x, y, z).setTypeIdAndData(blockId, (byte) data, false);
		}
	}

}
