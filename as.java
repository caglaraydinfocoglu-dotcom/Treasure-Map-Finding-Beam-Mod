package net.orteyt.tutorialmod.İTEM;

import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.checkerframework.checker.units.qual.K;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

@EventBusSubscriber(modid = "treasuremapfindingbeam", value = Dist.CLIENT)
public class as {
    public static boolean hazinebulundu = false;
    public static double hedefX = 0;
    public static double hedefZ = 0;
    public static net.minecraft.world.entity.Display.BlockDisplay aktifLazer = null;



    @SubscribeEvent
    public static void Lazer(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().level == null) return;
        if (hazinebulundu) {
               if(aktifLazer==null) {

                   Display.BlockDisplay lazer = EntityType.BLOCK_DISPLAY.create(Minecraft.getInstance().level);
                   CompoundTag etiket = new CompoundTag();
                   Objects.requireNonNull(lazer).saveWithoutId(etiket);
                   etiket.put("block_state", NbtUtils.writeBlockState(KodAyarlari.secilenRenk.defaultBlockState())); // Kimliğin içine zorla kırmızı camı yaz
                   Transformation trans = new Transformation(
                           new Vector3f(0f, 0f, 0f),        // translation
                           new Quaternionf(),                // leftRotation (identity)
                           new Vector3f(1.0F, KodAyarlari.lazerYuksekligi, 1.0F), // scale - burada Y'yi 380 kat uzatıp "lazer" görüntüsü veriyorsun, mantık doğru
                           new Quaternionf()                  // rightRotation (identity)
                   );
                   etiket.put("transformation", Transformation.CODEC.encodeStart(NbtOps.INSTANCE, trans).getOrThrow());
                   net.minecraft.nbt.CompoundTag isik = new net.minecraft.nbt.CompoundTag();
                   isik.putInt("block", 15);
                   isik.putInt("sky", 15);
                   etiket.put("brightness", isik);
                   etiket.putFloat("width", 1.0F);
                   etiket.putFloat("height", KodAyarlari.lazerYuksekligi);
                   lazer.load(etiket);
                   lazer.setPos(hedefX, 40, hedefZ);
                   Minecraft.getInstance().level.addEntity(lazer);
                   aktifLazer = lazer;

               }




        }else if(aktifLazer !=null) {
            aktifLazer.discard();
            aktifLazer=null;
        }


    }


    @SubscribeEvent
    public static void haritayiKontrolEt(ClientTickEvent.Post event) {
        assert Minecraft.getInstance().player != null;
        if (Minecraft.getInstance().player != null) {
            hazinebulundu = false;
            ItemStack eldekiEsya = Minecraft.getInstance().player.getMainHandItem();
            ItemStack solEldekiEsya= Minecraft.getInstance().player.getOffhandItem();

            if (eldekiEsya.is(Items.FILLED_MAP)) {
                if (eldekiEsya.has(DataComponents.MAP_DECORATIONS)) {
                    MapDecorations dekorasyon = eldekiEsya.get(DataComponents.MAP_DECORATIONS);
                    assert dekorasyon != null;
                    MapDecorations.Entry ilkdekorasyon = dekorasyon.decorations().values().iterator().next();
                    double ilkx = ilkdekorasyon.x();
                    double ilkz = ilkdekorasyon.z();
                    if(ilkdekorasyon.type().is(MapDecorationTypes.OCEAN_MONUMENT)){
                        double OMxchunk = Minecraft.getInstance().player.chunkPosition().x;
                        double OMzchunk = Minecraft.getInstance().player.chunkPosition().z;
                        double OMilkxchunk = ilkx / 16;
                        double OMilkzchunk = ilkz / 16;
                        double OMyakinmix = Math.abs(OMxchunk - OMilkxchunk);
                        double OMyakinmiz = Math.abs(OMzchunk - OMilkzchunk);
                        if (OMyakinmix <= 24 && OMyakinmiz <= 24) {
                            hazinebulundu = true;
                            hedefX = ilkx;
                            hedefZ = ilkz;
                        } else {
                            hazinebulundu = false;

                        }
                    }

                    if (ilkdekorasyon.type().is(MapDecorationTypes.RED_X) ){
                        double xchunk = Minecraft.getInstance().player.chunkPosition().x;
                        double zchunk = Minecraft.getInstance().player.chunkPosition().z;
                        double ilkxchunk = ilkx / 16;
                        double ilkzchunk = ilkz / 16;
                        double yakinmix = Math.abs(xchunk - ilkxchunk);
                        double yakinmiz = Math.abs(zchunk - ilkzchunk);
                        if (yakinmix <= 2 && yakinmiz <= 2) {
                            hazinebulundu = true;
                            hedefX = ilkx;
                            hedefZ = ilkz;
                        } else {
                            hazinebulundu = false;

                        }

                    }
                }


            } else if (solEldekiEsya.is(Items.FILLED_MAP)) {
                if(solEldekiEsya.has(DataComponents.MAP_DECORATIONS)){
                    MapDecorations soldekorasyon = solEldekiEsya.get(DataComponents.MAP_DECORATIONS);
                    MapDecorations.Entry solilkdekorasyon = soldekorasyon.decorations().values().iterator().next();
                    double solilkx = solilkdekorasyon.x();
                    double solilkz = solilkdekorasyon.z();
                    if(solilkdekorasyon.type().is(MapDecorationTypes.RED_X)){
                        double solxchunk = Minecraft.getInstance().player.chunkPosition().x;
                        double solzchunk = Minecraft.getInstance().player.chunkPosition().z;
                        double solilkxchunk = solilkx / 16;
                        double solilkzchunk = solilkz / 16;
                        double solyakinmix = Math.abs(solxchunk - solilkxchunk);
                        double solyakinmiz = Math.abs(solzchunk - solilkzchunk);
                        if(solyakinmix<= 2 && solyakinmiz<= 2){
                            hazinebulundu=true;
                            hedefX =solilkx;
                            hedefZ =solilkz;
                        }else{
                            hazinebulundu=false;
                        }
                    }


                }

            }
        }else {
                hazinebulundu = false;

        }


    }


}






