package com.vSmart.reelstar

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic
import ly.img.android.pesdk.assets.font.basic.FontPackBasic
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes
import ly.img.android.pesdk.backend.model.constant.OutputMode
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.VideoEditorSaveSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.*
import ly.img.android.pesdk.ui.panels.item.PersonalStickerAddItem

class FilterMainActivity : AppCompatActivity() {

    companion object {
        const val VESDK_RESULT = 1
        const val CAMERA_AND_GALLERY_RESULT = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.hasExtra("url")) {
            var url = intent.getStringExtra("url")
            var myUri = Uri.parse(url)
            System.out.println("url------->" + myUri)
            openEditor(myUri);
        }

    }

    private fun createVESDKSettingsList() =
        VideoEditorSettingsList(true)
            .configure<UiConfigFilter> {
                it.setFilterList(FilterPackBasic.getFilterPack())
            }
            .configure<UiConfigText> {
                it.setFontList(FontPackBasic.getFontPack())
            }
            .configure<UiConfigFrame> {
                it.setFrameList(FramePackBasic.getFramePack())
            }
            .configure<UiConfigOverlay> {
                it.setOverlayList(OverlayPackBasic.getOverlayPack())
            }
            .configure<UiConfigSticker> {
                it.setStickerLists(
                    PersonalStickerAddItem(),
                    StickerPackEmoticons.getStickerCategory(),
                    StickerPackShapes.getStickerCategory()
                    //  StickerPackAnimated.getStickerCategory()
                )
            }
            .configure<VideoEditorSaveSettings> {
                // Set custom editor video export settings
                it.setOutputToGallery(Environment.DIRECTORY_DCIM)
                it.outputMode = OutputMode.EXPORT_IF_NECESSARY
            }
    fun openEditor(inputSource: Uri?) {
        val settingsList = createVESDKSettingsList()

        settingsList.configure<LoadSettings> {
            it.source = inputSource
        }

        VideoEditorBuilder(this)
            .setSettingsList(settingsList)
            .startActivityForResult(this, VESDK_RESULT)

        settingsList.release()
    }

}