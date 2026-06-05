import axios from 'axios';

const baseApiUrl = 'api/batch';

export default class BatchService {
  exportPersons(): Promise<Blob> {
    return axios.get(`${baseApiUrl}/export/persons`, { responseType: 'blob' }).then(res => res.data);
  }

  exportPositions(): Promise<Blob> {
    return axios.get(`${baseApiUrl}/export/positions`, { responseType: 'blob' }).then(res => res.data);
  }

  exportSkills(): Promise<Blob> {
    return axios.get(`${baseApiUrl}/export/skills`, { responseType: 'blob' }).then(res => res.data);
  }

  importPersons(file: File): Promise<string> {
    const form = new FormData();
    form.append('file', file);
    return axios.post(`${baseApiUrl}/import/persons`, form).then(res => res.data);
  }

  importPositions(file: File): Promise<string> {
    const form = new FormData();
    form.append('file', file);
    return axios.post(`${baseApiUrl}/import/positions`, form).then(res => res.data);
  }

  importSkills(file: File): Promise<string> {
    const form = new FormData();
    form.append('file', file);
    return axios.post(`${baseApiUrl}/import/skills`, form).then(res => res.data);
  }
}
