package net.orteyt.tutorialmod.İTEM;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = "treasuremapfindingbeam", value = Dist.CLIENT)
public class KodAyarlari {
    public static net.minecraft.world.level.block.Block secilenRenk = net.minecraft.world.level.block.Blocks.RED_STAINED_GLASS;
    public static float lazerYuksekligi = 90.0F;
    @SubscribeEvent
    public static void komutlariKaydet(net.neoforged.neoforge.client.event.RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
                net.minecraft.commands.Commands.literal("color")
                        .then(net.minecraft.commands.Commands.literal("red")
                                .executes(context -> {
                                    renkGuncelle(net.minecraft.world.level.block.Blocks.RED_STAINED_GLASS);
                                    context.getSource().sendSystemMessage(net.minecraft.network.chat.Component.literal("§cLazer rengi KIRMIZI olarak ayarlandı."));
                                    return 1;
                                }))
                        .then(net.minecraft.commands.Commands.literal("blue")
                                .executes(context -> {
                                    renkGuncelle(net.minecraft.world.level.block.Blocks.BLUE_STAINED_GLASS);
                                    // Chat penceresinde mavi yazıyla bilgi verir
                                    context.getSource().sendSystemMessage(net.minecraft.network.chat.Component.literal("§9Lazer rengi MAVİ olarak ayarlandı."));
                                    return 1;
                                }))
                        .then(net.minecraft.commands.Commands.literal("yellow")
                                .executes(context -> {
                                    renkGuncelle(net.minecraft.world.level.block.Blocks.YELLOW_STAINED_GLASS);
                                    // Chat penceresinde sarı yazıyla bilgi verir
                                    context.getSource().sendSystemMessage(net.minecraft.network.chat.Component.literal("§eLazer rengi SARI olarak ayarlandı."));
                                    return 1;
                                })));
                event.getDispatcher().register(
                        Commands.literal("length")
                                .then(Commands.argument("deger", IntegerArgumentType.integer(1, 300))
                                        .executes(context -> {
                                            int yeniBoy = IntegerArgumentType.getInteger(context, "deger");
                                            yukseklikGuncelle((float) yeniBoy);
                                            context.getSource().sendSystemMessage(net.minecraft.network.chat.Component.literal("§aLazer uzunluğu " + yeniBoy + " blok olarak ayarlandı."));
                                            return 1;
                                        }))
                );

    }

    public static void renkGuncelle(net.minecraft.world.level.block.Block yeniRenk) {
        secilenRenk = yeniRenk;
        if (as.aktifLazer != null) {
            as.aktifLazer.discard();
            as.aktifLazer = null;

            // Eğer oyuncu haritaya bakıyorken komutu yazarsa, lazerin renginin anında değişmesi için
            // mevcut lazeri siliyoruz. Tick event onu yeni renkle hemen geri getirece
        }

    }
    public static void yukseklikGuncelle(float yeniYukseklik) {
        lazerYuksekligi = yeniYukseklik;
        if (as.aktifLazer != null) {
            as.aktifLazer.discard();
            as.aktifLazer = null;
        }
    }
}
