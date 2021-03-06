package me.sub.angels.proxy;

import me.sub.angels.client.renders.entities.RenderAngelPainting;
import me.sub.angels.client.renders.entities.RenderAnomaly;
import me.sub.angels.client.renders.entities.RenderChronodyneGenerator;
import me.sub.angels.client.renders.entities.RenderWeepingAngel;
import me.sub.angels.client.renders.tileentities.RenderTileEntityCG;
import me.sub.angels.client.renders.tileentities.RenderTileEntityPlinth;
import me.sub.angels.client.renders.tileentities.RenderTileEntitySnowArm;
import me.sub.angels.common.entities.EntityAngelPainting;
import me.sub.angels.common.entities.EntityAnomaly;
import me.sub.angels.common.entities.EntityChronodyneGenerator;
import me.sub.angels.common.entities.EntityWeepingAngel;
import me.sub.angels.common.tileentities.TileEntityChronodyneGenerator;
import me.sub.angels.common.tileentities.TileEntityPlinth;
import me.sub.angels.common.tileentities.TileEntitySnowArm;
import me.sub.angels.utils.RenderUtil;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();
		entityRenders();
	}

	@Override
	public void init() {
		super.init();
		tileRenders();
	}

	@Override
	public void postInit() {
		super.postInit();
	}

	private void tileRenders() {
		RenderUtil.bindTESR(TileEntitySnowArm.class, new RenderTileEntitySnowArm());
		RenderUtil.bindTESR(TileEntityChronodyneGenerator.class, new RenderTileEntityCG());
		RenderUtil.bindTESR(TileEntityPlinth.class, new RenderTileEntityPlinth());
	}

	private void entityRenders() {
		RenderUtil.bindEntityRender(EntityWeepingAngel.class, RenderWeepingAngel::new);
		RenderUtil.bindEntityRender(EntityAngelPainting.class, RenderAngelPainting::new);
		RenderUtil.bindEntityRender(EntityAnomaly.class, RenderAnomaly::new);
		RenderUtil.bindEntityRender(EntityChronodyneGenerator.class, RenderChronodyneGenerator::new);
	}
}
