package ar.uba.fi.tallerii.comprameli.presentation.search

import ar.uba.fi.tallerii.comprameli.presentation.base.BasePresenter

class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    override fun onInit() {
        getView()?.refreshList(listOf(
                SearchContract.SearchItem(
                        0,
                        listOf("https://http2.mlstatic.com/funda-tpu-fibra-carbono-rugged-para-moto-x4-D_NQ_NP_721558-MLA25990289021_092017-F.jpg"),
                        "Moto G 6ta Generacion",
                        6900f
                ),
                SearchContract.SearchItem(
                        1,
                        listOf("https://http2.mlstatic.com/casco-moto-ls2-rebatible-doble-visor-370-easy-negro-mate-D_NQ_NP_770513-MLA25748947713_072017-F.jpg"),
                        "Casco L2S",
                        1500f
                ),
                SearchContract.SearchItem(
                        2,
                        listOf("https://http2.mlstatic.com/hidrolavadora-liliana-hidroclean-lh140-20-140-bar-2000w-D_NQ_NP_980409-MLA27980711330_082018-F.jpg"),
                        "Hidrolavadora NUEVA",
                        800f
                ),
                SearchContract.SearchItem(
                        3,
                        listOf("https://http2.mlstatic.com/mixer-liliana-masterpic-ah130-450w-2-velocidades-licua-bate-D_NQ_NP_846806-MLA27963185499_082018-F.jpg"),
                        "Procesadora ultima generacion",
                        1003f
                ),
                SearchContract.SearchItem(
                        4,
                        listOf("https://http2.mlstatic.com/fiat-punto-14-attractive-2011-D_NQ_NP_618652-MLA28402829958_102018-F.jpg"),
                        "Fiat Punto 1.4 Attractive 0Km",
                        300000f
                ),
                SearchContract.SearchItem(
                        5,
                        listOf("https://http2.mlstatic.com/colchon-simmons-backcare-extra-firm-2-plazas-190x140-D_NQ_NP_828615-MLA25289479700_012017-F.jpg"),
                        "Colchón Simmons Backcare - Extra Firm 2 Plazas 190x140",
                        5500f
                )
        ))
    }

}