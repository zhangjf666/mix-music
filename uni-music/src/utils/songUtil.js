
export function handleSingerName(song) {
    let artists = song.singers.map(item => {
        return item.singerName + '/';
    });
    artists = artists.join('');
    artists = artists.slice(0, artists.length - 1);
    return artists;
}