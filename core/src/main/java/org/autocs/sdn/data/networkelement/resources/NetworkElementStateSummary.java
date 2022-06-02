/*
 * Title:        SDN Package
 * Description:  SDN package of Auto Cloud Simulator (AutoCS)
 * License:      MIT
 *
 * Copyright (C) 2022 Ibrahem Mouhamad
 * Email: ibrahem.y.mouhamad@gmail.com
 */

package org.autocs.sdn.data.networkelement.resources;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/*
* Computes resource utilization statistics for a given {@link NetworkElement}.
* @author Ibrahem Mouhamad
* @since AutoCS SDN Package 1.0.0
*/

public class NetworkElementStateSummary {
    private final SummaryStatistics upChannels;
    private final SummaryStatistics upload;
    private final SummaryStatistics downChannels;
    private final SummaryStatistics download;

    public NetworkElementStateSummary() {
        upChannels = new SummaryStatistics();
        upload = new SummaryStatistics();
        downChannels = new SummaryStatistics();
        download = new SummaryStatistics();
    }

    public SummaryStatistics getUpChannels() {
        return upChannels;
    }

    public SummaryStatistics getUpload() {
        return upload;
    }

    public SummaryStatistics getDownChannels() {
        return downChannels;
    }

    public SummaryStatistics getDownload() {
        return download;
    }

    public void addValue(final long upChannels, final long upload, final long downChannels,
            final long download) {
        this.upChannels.addValue(upChannels);
        this.upload.addValue(upload);
        this.downChannels.addValue(downChannels);
        this.download.addValue(download);
    }
}
