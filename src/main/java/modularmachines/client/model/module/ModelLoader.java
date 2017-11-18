package modularmachines.client.model.module;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.api.modules.Module;
import modularmachines.api.modules.model.IModelData;
import modularmachines.api.modules.model.IModuleModelState;
import modularmachines.common.ModularMachines;

@SideOnly(Side.CLIENT)
public enum ModelLoader {
	INSTANCE;
	
	@Nullable
	private static ImmutableMap<ResourceLocation, ImmutableMap<VertexFormat, IBakedModel>> bakedModels;
	private static final Cache<Pair<ResourceLocation, IModuleModelState>, IBakedModel> cachedModels =
			CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();
	
	@SuppressWarnings("unchecked")
	public void reloadModels() {
		List<ResourceLocation> modelLocations = new ArrayList<>();
		Map<ResourceLocation, Exception> loadingExceptions = Maps.newHashMap();
		Builder<ResourceLocation, ImmutableMap<VertexFormat, IBakedModel>> modelBaker = new Builder<>();
		ModularMachines.dataRegistry.getValues().stream()
				.filter(Objects::nonNull)
				.map(data -> {
					IModelData modelData = data.getModel();
					if (modelData == null) {
						return Collections.<ResourceLocation>emptyList();
					}
					return modelData.locations().values();
				})
				.flatMap(Collection::stream)
				.filter(l -> Objects.nonNull(l) && !modelLocations.contains(l))
				.forEach(location -> {
					Builder<VertexFormat, IBakedModel> baker = new Builder<>();
					IModel model = null;
					try {
						model = ModelLoaderRegistry.getModel(location);
					} catch (Exception e) {
						loadingExceptions.put(location, e);
					}
					if (model != null) {
						baker.put(DefaultVertexFormats.BLOCK, model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, DefaultTextureGetter.INSTANCE));
						//baker.put(DefaultVertexFormats.ITEM, model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, DefaultTextureGetter.INSTANCE));
						modelBaker.put(location, baker.build());
						modelLocations.add(location);
					}
					/*ResourceLocation windowLocation = module.getWindowLocation(data);
					if (windowLocation != null && !modelLocations.contains(windowLocation)) {
						Builder<VertexFormat, IBakedModel> windowBaker = new Builder<>();
						IModel model;
						try {
							model = ModelLoaderRegistry.getModel(windowLocation);
						} catch (Exception exceptionModel) {
							loadingExceptions.put(windowLocation, exceptionModel);
							model = null;
						}
						if (model != null) {
							windowBaker.put(DefaultVertexFormats.BLOCK, model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, DefaultTextureGetter.INSTANCE));
							windowBaker.put(DefaultVertexFormats.ITEM, model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, DefaultTextureGetter.INSTANCE));
							modelBaker.put(windowLocation, windowBaker.build());
							modelLocations.add(windowLocation);
						}
					}*/
				});
		ModelLoader.bakedModels = modelBaker.build();
		cachedModels.invalidateAll();
		loadingExceptions.values().forEach(Throwable::printStackTrace);
	}
	
	public enum DefaultTextureGetter implements Function<ResourceLocation, TextureAtlasSprite> {
		INSTANCE;
		
		@Override
		public TextureAtlasSprite apply(ResourceLocation location) {
			return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
		}
	}
	
	@Nullable
	public static IBakedModel getModel(Module module, IModelState modelState, VertexFormat vertex) {
		IModuleModelState moduleModelState = module.getModelState();
		IBakedModel model = cachedModels.getIfPresent(Pair.of(module.getData().getRegistryName(), moduleModelState));
		IModelData data = getModelData(module);
		if (data == null) {
			return null;
		}
		if (module.isModelNeedReload() || model == null) {
			ModelList modelList = new ModelList(data.locations(), vertex, modelState, DefaultTextureGetter.INSTANCE);
			module.setModelState(moduleModelState = data.createState(module));
			data.addModel(modelList, module, moduleModelState);
			if (modelList.empty()) {
				model = modelList.missingModel();
			} else {
				model = BakedMultiModel.create(modelList.models());
			}
			cachedModels.put(Pair.of(module.getData().getRegistryName(), moduleModelState), model);
			module.setModelNeedReload(false);
		}
		return model;
	}
	
	@Nullable
	private static IModelData getModelData(@Nullable Module module) {
		if (module == null) {
			return null;
		}
		return module.getData().getModel();
	}
	
	@Nullable
	public static IBakedModel getModel(ResourceLocation location, VertexFormat format) {
		if (bakedModels == null || !bakedModels.containsKey(location)) {
			return null;
		}
		if (!bakedModels.get(location).containsKey(format)) {
			return null;
		}
		return bakedModels.get(location).get(format);
	}
}
